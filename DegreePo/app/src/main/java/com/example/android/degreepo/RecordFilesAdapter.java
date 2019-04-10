package com.example.android.degreepo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class RecordFilesAdapter extends RecyclerView.Adapter<RecordFilesAdapter.FileViewHolder> {

    /**
     * For this adapter, we should override 3 functions
     * 1. onCreateViewHolder: With two parameters and return value
     * @param viewGroup : The views on Recycler view
     * @param viewType
     * @return The view holder of this recycler view
     *
     * 2. getItemCount(): If there is no file, return 0. Or return the length of file
     * @return The length of file or 0.
     *
     * 3. onBindViewHolder: Two parameters and no return value
     * @param holder : View holder for display the text view of file names
     * @param position : The index of display.
     */

    private String[] rFiles;
    private final RecordFilesOnClickListener rFileOnclick;

    /**
     * Create an interface to receive the onClick messages.
     */
    public interface RecordFilesOnClickListener{
        void AdapterOnClick(String fileName);
        //void AdapterLongOnClick(View view, String fileName);
    }


    public RecordFilesAdapter (RecordFilesOnClickListener rfOnClick){
        rFileOnclick = rfOnClick;
    }

    @Override
    public int getItemCount(){

        if (rFiles == null)
            return 0;

        return rFiles.length;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        Context context = viewGroup.getContext();
        int list_layout_id = R.layout.recording_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParentImmediately = false;

        View view = inflater.inflate(list_layout_id, viewGroup, attachToParentImmediately);

        return new FileViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position){

        final String theFileName = rFiles[position];
        holder.fileNames.setText(theFileName);
        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                rFileOnclick.AdapterLongOnClick(holder.itemView, theFileName);
                return false;
            }
        });*/

    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView fileNames;

        private FileViewHolder (View itemView){
            super(itemView);
            fileNames = itemView.findViewById(R.id.file_names);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){

            int fileAdapterPosition = getAdapterPosition();
            String fName = rFiles[fileAdapterPosition];
            rFileOnclick.AdapterOnClick(fName);

        }

    }

    public void setFileNames(String[] fileNames){
        rFiles = fileNames;
        notifyDataSetChanged();
    }

    // Remove files
    public void removeFiles(String fileName){

        File removef = new File(fileName);
        if (removef.exists() && removef.isFile())
            removef.delete();

        if (removef.delete())
            Log.e("REMOVE_FILE",fileName + " is removed");
        else
            Log.e("REMOVE_FILE",fileName + " removing failed");
        //notifyDataSetChanged();
    }

}
