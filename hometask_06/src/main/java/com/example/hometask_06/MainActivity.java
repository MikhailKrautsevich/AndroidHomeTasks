package com.example.hometask_06;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.hometask_06.database.ConDataBase;
import com.example.hometask_06.database.ContactDao;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerContacts;
    private TextView noContacts;
    private static final int ADD_NEW_CONTACT = 900 ;
    private static final int EDIT_CONTACT = 901 ;
    private LiveData<List<ContactClass>> contacts ;
    private NameListAdapter adapter1;
    private LinearLayoutManager linearLayoutManager ;
    private GridLayoutManager gridLayoutManager ;
    private ConDataBase dataBase ;
    private ContactDao contactDao ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = Room.databaseBuilder(this, ConDataBase.class, "database")
                .build() ;
        contactDao = dataBase.getConDao() ;

        noContacts = findViewById(R.id.noContacts) ;
        ImageButton addNewContact = findViewById(R.id.addNewContact) ;
        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityAdd.class) ;
                startActivityForResult(intent, ADD_NEW_CONTACT);
            }
        });

        if (linearLayoutManager == null) {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);}
        if (gridLayoutManager == null)  {
        gridLayoutManager = new GridLayoutManager(this, 2);}

        recyclerContacts = findViewById(R.id.recyclerContacts);
        recyclerContacts.setAdapter(new NameListAdapter(new ArrayList<ContactClass>()));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {recyclerContacts.setLayoutManager(linearLayoutManager);}
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {recyclerContacts.setLayoutManager(gridLayoutManager) ;}
        recyclerContacts.setVisibility(View.INVISIBLE);
        if (adapter1==null) {
            adapter1 = (NameListAdapter) recyclerContacts.getAdapter();
        }

        SearchView searchView = findViewById(R.id.search) ;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter1.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        noContacts.setVisibility(View.VISIBLE);
        recyclerContacts.setVisibility(View.INVISIBLE);
        contacts = contactDao.getAllContacts();
        contacts.observe(this, new Observer<List<ContactClass>>() {
            @Override
            public void onChanged(List<ContactClass> contactClasses) {
                recyclerContacts.setAdapter(new NameListAdapter(contactClasses));
                recyclerContacts.setVisibility(View.VISIBLE);
                noContacts.setVisibility(View.INVISIBLE);
            }
        });
        if (adapter1 != null && !adapter1.isListOfContactsEmpty()) {
            noContacts.setVisibility(View.GONE);
            recyclerContacts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ADD_NEW_CONTACT): {
                boolean is = data.getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false);
                if (!(data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME) == null) &&
                        !(data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO) == null)) {
                    String conName = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME).trim();
                    String conInfo = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO).trim();

                    if (!conInfo.isEmpty() && !conName.isEmpty()) {
                    final ContactClass newContact = new ContactClass(conName, conInfo, is);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            contactDao.addContact(ContactClass.createEntity(newContact));
                        }
                    }) ;
                    thread.start();

                    if (!conInfo.isEmpty() && !conName.isEmpty()) {

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.somethingwrongwithc, LENGTH_SHORT)
                                .show();
                    }
                }}
                break;
            }
            case (EDIT_CONTACT): {
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactDao = null ;
        dataBase.close();
        dataBase = null ;
    }

    class NameListAdapter extends RecyclerView.Adapter<NameListAdapter.ItemViewHolder> implements Filterable {

        List<ContactClass> contactsFull ;

        @RequiresApi(api = Build.VERSION_CODES.N)
        NameListAdapter(List<ContactClass> contactsFull) {
            this.contactsFull = contactsFull;
            this.contactsFull.sort(new ContactsComparator());
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.contact_element, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NameListAdapter.ItemViewHolder holder, int position) {
            holder.bindData(contactsFull.get(position));
        }

        private boolean isListOfContactsEmpty(){
            return contactsFull.isEmpty();
        }

        @Override
        public int getItemCount() {if (contacts != null)
            return contactsFull.size();
        else {
            return 0;}
        }

        @Override
        public Filter getFilter() {
            return contactFilter;
        }

        private Filter contactFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ContactClass> contactsFiltered = new ArrayList<>();
                if (constraint == null || constraint.length()==0) {
                    contactsFiltered.addAll(contactsFull); }
                else {
                    String pattern = constraint.toString().toLowerCase().trim();
                    for (ContactClass con : contactsFull) {
                        if (con.getName().toLowerCase().startsWith(pattern)) {
                            contactsFiltered.add(con) ;
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = contactsFiltered ;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactsFull.clear();
                contactsFull.addAll( (List) results.values);
                notifyDataSetChanged();
            }
        } ;

        class ItemViewHolder extends RecyclerView.ViewHolder{
            private TextView nameText;
            private TextView infoText;
            private ImageView phNumberPic, emailPic ;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        ContactClass contactClass = contactsFull.get(position) ;
 //                       Toast.makeText(context, contactClass.getName(), LENGTH_SHORT).show();
                        Intent remIntent = new Intent(MainActivity.this, ActivityEdit.class);
                        remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, contactClass.getName());
                        remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, contactClass.getNumberOrEmail());
                        remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, contactClass.isEmail);
                        remIntent.putExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, position) ;
                        startActivityForResult(remIntent, EDIT_CONTACT);
                    }
                });
                phNumberPic = itemView.findViewById(R.id.phNumberPic) ;
                emailPic = itemView.findViewById(R.id.emailPic) ;
                nameText = itemView.findViewById(R.id.nameText);
                infoText = itemView.findViewById(R.id.emailText);
            }

            void bindData(ContactClass contact) {
                nameText.setText(contact.getName());
                infoText.setText(contact.getNumberOrEmail());
                if (contact.isEmail) {phNumberPic.setVisibility(View.INVISIBLE);
                                        emailPic.setVisibility(View.VISIBLE);
                                        nameText.setTextColor(Color.GREEN);}
                if (!contact.isEmail) {phNumberPic.setVisibility(View.VISIBLE);
                                        emailPic.setVisibility(View.INVISIBLE);
                                        nameText.setTextColor(Color.CYAN);}}
        }
        }
    }




