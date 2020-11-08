package com.example.connectme;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";
    List<String> nameList;
    List<String> nameListAll;
    List<String> addressList;

    public RecyclerAdapter(List<String> nameList, List<String> addressList) {
        this.nameList = nameList;
        this.addressList = addressList;
        nameListAll = new ArrayList<>();
        nameListAll.addAll(nameList);
        addressList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        change to retrieve address from api
        holder.rowCountTextView.setText(addressList.get(position));
        holder.textView.setText(nameList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    @Override
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(nameListAll);
            } else {
                for (String name: nameListAll) {
                    if (name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(name);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            nameList.clear();
            nameList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, rowCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), nameList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            String select = nameList.get(getAdapterPosition()); // change to polyline list
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            intent.putExtra("Location Selected", select);
            // String select = getIntent().getStringExtra("Location Selected"); //use in maps activity to obtain
            view.getContext().startActivity(intent);
        }
    }
}

