package com.example.momomanagement.Agent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.momomanagement.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> implements Filterable {


    private TransactionListener transactionListener;
    public static boolean transactionSearch = false;
    private List<TransactionModel> mTubeList;
    private List<TransactionModel> mTubeListFiltered;

    private String networkType, userType;

    public TransactionAdapter(TransactionListener transactionListener, List<TransactionModel> tubeList, String networkType, String userType) {
        this.transactionListener = transactionListener;
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
        this.networkType = networkType;
        this.userType = userType;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_view_layout, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TransactionModel model = mTubeListFiltered.get(position);
        holder.actualTransType.setText(model.getTransactionType());
        holder.transAmount.setText(String.valueOf(model.getAmountSentWithdrawn()));
        holder.tEarns.setText("+ GHS "+String.valueOf(model.getCommissionEarned()));
        holder.number.setText(model.getPhoneNumber());
        CharSequence charSequence = DateFormat.format("MMM d, yyyy h:mm:ss a", model.getDateTime().toDate());
        holder.dateTime.setText(charSequence);


        if (model.getTransactionType().equals("Withdrawal")){
            holder.transTypeDesc.setText("Cash-Out");
        } else if (model.getTransactionType().equals("Airtime") || model.getTransactionType().equals("Bundle")){
            holder.transTypeDesc.setText("Transfer");
        }else{
            holder.transTypeDesc.setText("Cash-In");
        }

        if (networkType.equals("Telecel")){
            holder.transIV.setImageResource(R.drawable.telecellogo);
        }else if (networkType.equals("AirtelTigo")){
            holder.transIV.setImageResource(R.drawable.airteltiglogo);
        }else{
            holder.transIV.setImageResource(R.drawable.mtn_circle_icon);
        }


        holder.transCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionListener.transactionClick(holder.getAdapterPosition(), model);
            }
        });

        if (userType.equals("Agent")){
            holder.transCL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(view.getContext()).setTitle("Delete")
                            .setMessage("Are you sure you want to delete this transaction?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    transactionListener.transactionDelete(holder.getAdapterPosition(), model);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                    return false;

                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return mTubeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String pattern = charSequence.toString().toLowerCase();
                if(pattern.isEmpty()){
                    mTubeListFiltered = mTubeList;
                } else {
                    List<TransactionModel> filteredList = new ArrayList<>();
                    for(TransactionModel tube: mTubeList){
                        if(transactionSearch == false) {
                            if (tube.getPhoneNumber().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                        else {
                            if (tube.getTransactionType().toLowerCase().contains(pattern) || tube.getTransactionType().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                    }
                    mTubeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTubeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTubeListFiltered = (ArrayList<TransactionModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView transTypeDesc, actualTransType, transAmount, number, tEarns, dateTime;
        private ImageView transIV;

        private ConstraintLayout transCL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           transTypeDesc = itemView.findViewById(R.id.transTypeDesc);
           actualTransType = itemView.findViewById(R.id.actualTransaction);
           transAmount = itemView.findViewById(R.id.transactionAmount);
           number = itemView.findViewById(R.id.phoneNumber);
           dateTime = itemView.findViewById(R.id.transDateTime);
           tEarns = itemView.findViewById(R.id.transEarning);
           transIV = itemView.findViewById(R.id.transIV);
           transCL = itemView.findViewById(R.id.transCL);

        }

    }

    public interface TransactionListener{
        void transactionDelete(int position, TransactionModel model);
        void transactionClick(int position, TransactionModel model);
    }
}
