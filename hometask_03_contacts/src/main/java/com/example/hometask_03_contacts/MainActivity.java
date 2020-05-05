package com.example.hometask_03_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerContacts;
    private TextView noContacts;
    static final int ADD_NEW_CONTACT = 900 ;
    static final int EDIT_CONTACT = 901 ;
    static ArrayList<ContactClass> contacts = new ArrayList<>();
    NameListAdapter adapter1;
    LinearLayoutManager linearLayoutManager ;
    GridLayoutManager gridLayoutManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noContacts = findViewById(R.id.noContacts) ;

        ImageButton addNewContact = findViewById(R.id.addNewContact) ;
        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_add.class) ;
                startActivityForResult(intent, ADD_NEW_CONTACT);
            }
        });

        if (linearLayoutManager == null) {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);}
        if (gridLayoutManager == null)  {
        gridLayoutManager = new GridLayoutManager(this, 2);}

        recyclerContacts = findViewById(R.id.recyclerContacts);
        recyclerContacts.setAdapter(new NameListAdapter(this));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {recyclerContacts.setLayoutManager(linearLayoutManager);}
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {recyclerContacts.setLayoutManager(gridLayoutManager) ;}
        recyclerContacts.setVisibility(View.INVISIBLE);
        if (adapter1==null)
        adapter1 = (NameListAdapter) recyclerContacts.getAdapter();

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
        if (adapter1 != null && adapter1.isListOfContactsEmpty()) {
            recyclerContacts.setVisibility(View.INVISIBLE);
            noContacts.setVisibility(View.VISIBLE);
        } else {
            recyclerContacts.setVisibility(View.VISIBLE);
            noContacts.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NameListAdapter adapter1 = (NameListAdapter) recyclerContacts.getAdapter();
        switch(requestCode) {
            case (ADD_NEW_CONTACT): {
                boolean is = data.getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false);
                if (!(data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME) == null) &&
                        !(data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO) == null)) {
                    String conName = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME).trim();
                    String conInfo = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO).trim();

                    if (!conInfo.isEmpty() && !conName.isEmpty()) {
                    ContactClass newContact = new ContactClass(conName, is, conInfo);

                    if (!conInfo.isEmpty() && !conName.isEmpty()) {
                        assert adapter1 != null;
                        adapter1.addItem(newContact);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.somethingwrongwithc, LENGTH_SHORT)
                                .show();
                    }
                }}
                break;
            }
            case (EDIT_CONTACT): {
                if (resultCode == RESULT_OK) {
//                    Toast.makeText(this, "Try to remove", LENGTH_SHORT).show();
                    int position = data.getIntExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, 9);
                    assert adapter1 != null;
                    adapter1.removeItem(position);
                }
                break;
            }
            default:
                break;
        }
    }

    class NameListAdapter extends RecyclerView.Adapter<NameListAdapter.ItemViewHolder> implements Filterable {

        ArrayList<ContactClass> contactsFull ;

        NameListAdapter(Context context) {
            contactsFull = new ArrayList<>(contacts);
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
            holder.bindData(contacts.get(position));
        }

        private boolean isListOfContactsEmpty(){
            return contacts.isEmpty();
        }

        private void addItem(ContactClass newContact) {
            contacts.add(newContact);
//            notifyItemChanged(items.indexOf(name)); // for item
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ContactsComparator comparator = new ContactsComparator() ;
                contacts.sort(comparator);
            }
            notifyDataSetChanged(); // for all items
        }

        @Override
        public int getItemCount() {if (contacts != null)
            return contacts.size();
        else {
            return 0;}
        }

        void removeItem(int position) {
            if (contacts.size() >= position)
            {contacts.remove(position);}
            notifyDataSetChanged();
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
                contacts.clear();
                contacts.addAll( (List) results.values);
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
                        ContactClass contactClass = contacts.get(position) ;
 //                       Toast.makeText(context, contactClass.getName(), LENGTH_SHORT).show();
                        Intent remIntent = new Intent(MainActivity.this, Activity_edit.class);
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




