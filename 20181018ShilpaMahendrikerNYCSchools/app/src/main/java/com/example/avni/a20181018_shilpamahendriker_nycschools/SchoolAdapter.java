package com.example.avni.a20181018_shilpamahendriker_nycschools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;



    public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {
        //this context used to inflate the layout
        private Context mContext;

        //storing all the Locations in a list
        private ArrayList<School> Schools;

        //getting the context and Locations list with constructor
        public SchoolAdapter(Context mContext, ArrayList<School> Schools) {
            this.mContext = mContext;
            this.Schools = Schools;

        }

        @Override
        public SchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflating and returning the view holder

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.list_item, parent,false);
            return new SchoolViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SchoolViewHolder holder, final int position) {
            //getting the location of the specified position
            final School School = Schools.get(position);

            //binding the data with the view holder views
            holder.textSchoolName.setText(School.getSchoolName());
            holder.textSchoolBorough.setText("Borough: " + School.getSchoolBorough());




            //Creating on click listener for the item selected on the view holder
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    School selection = Schools.get(position);

                    // Sending DBN, Location and school name through the intent to school details activity
                    Intent intent = new Intent(mContext, SchoolDetailsActivity.class);
                    intent.putExtra("DBN", selection.getBdn());
                    intent.putExtra("LOCATION", selection.getSchoolLocation());
                    intent.putExtra("NAME",selection.getSchoolName());
                    mContext.startActivity(intent);
                }
            });

        }

        /*Method to get the number of items in the locations array*/
        @Override
        public int getItemCount() {
            return Schools.size();
        }


        /*Providing reference to each of the views within a data item */

        class SchoolViewHolder extends RecyclerView.ViewHolder {
            TextView textSchoolName,textSchoolBorough;


            /*constructor that accepts the entire item row and lookups to find each subview*/

            public SchoolViewHolder(View itemView) {
                super(itemView);
                textSchoolName = itemView.findViewById(R.id.txtSchoolName);
                textSchoolBorough = itemView.findViewById(R.id.txtBorough);


            }
        }
}