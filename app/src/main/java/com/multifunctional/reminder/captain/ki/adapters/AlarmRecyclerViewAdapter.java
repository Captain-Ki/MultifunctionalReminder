package com.multifunctional.reminder.captain.ki.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.multifunctional.reminder.captain.ki.R;
import com.multifunctional.reminder.captain.ki.utils.AlarmsManager;

import java.util.ArrayList;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context context;
    private ArrayList<Integer> id;
    private ArrayList<String> hours;
    private ArrayList<String> minutes;
    private ArrayList<String> names;
    private ArrayList<String> active;


    public AlarmRecyclerViewAdapter(Context context, ArrayList<Integer> id, ArrayList<String> hours, ArrayList<String> minutes,
                                    ArrayList<String> names, ArrayList<String> active) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.id = id;
        this.hours = hours;
        this.minutes = minutes;
        this.names = names;
        this.active = active;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.alarms_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        String hoursStr, minutesStr;

        if ((Integer.parseInt(hours.get(position))) < 10) {
            hoursStr = "0" + hours.get(position);
        } else {
            hoursStr = hours.get(position);
        }

        if ((Integer.parseInt(minutes.get(position))) < 10) {
            minutesStr = "0" + minutes.get(position);
        } else {
            minutesStr = minutes.get(position);
        }

        holder.timeTextView.setText(hoursStr + " : " + minutesStr);
        holder.nameTextView.setText(names.get(position));

        if (active.get(position).equals("1")) {
            holder.activeSwitch.setChecked(true);
        } else {
            holder.activeSwitch.setChecked(false);
        }
        holder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmsManager.UpdateAlarm(context,
                        id.get(position),
                        Integer.parseInt(hours.get(position)),
                        Integer.parseInt(minutes.get(position)),
                        names.get(position),
                        isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public Integer getItem(int id) {
        return this.id.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timeTextView;
        TextView nameTextView;
        Switch activeSwitch;

        ViewHolder(View itemView) {
            super(itemView);

            timeTextView = itemView.findViewById(R.id.alarm_item_time);
            nameTextView = itemView.findViewById(R.id.alarm_item_name);
            activeSwitch = itemView.findViewById(R.id.alarm_item_active);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}