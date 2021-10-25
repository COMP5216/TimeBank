package comp5216.sydney.edu.au.timebank.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import comp5216.sydney.edu.au.timebank.R;
import comp5216.sydney.edu.au.timebank.model.Task;

public class TaskAdapter extends FirestoreAdapter<TaskAdapter.ViewHolder> {

    private OnTaskSelectedListener mListener;

    public TaskAdapter(Query query,OnTaskSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    public interface OnTaskSelectedListener {

        void onTaskSelected(DocumentSnapshot task);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userView;
        TextView priceView;
        TextView infoView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.task_item_image);
            userView=itemView.findViewById(R.id.task_item_name);
            priceView = itemView.findViewById(R.id.task_item_coins);
            infoView=itemView.findViewById(R.id.task_item_info);
        }

        public void bind(final DocumentSnapshot snapshot, final OnTaskSelectedListener mListener){
            Task task = snapshot.toObject(Task.class);
            Resources resource = itemView.getResources();

            Glide.with(imageView.getContext()).load(task.getPhoto()).into(imageView);

            userView.setText(task.getName());
            priceView.setText(Integer.toString(task.getTime_coin()));
            infoView.setText(task.getDescription());

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        mListener.onTaskSelected(snapshot);
                    }
                }
            });

        }
    }
}
