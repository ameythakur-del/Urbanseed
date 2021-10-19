package ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.groceries.urabanseed.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Image;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<Image> snacksList = new ArrayList<>();
    String TAG = "Adapter";

    public SliderAdapterExample(Context context, List<Image> snacksList) {
        this.context = context;
        this.snacksList = snacksList;
    }

    public void renewItems(List<Image> snacksList) {
        this.snacksList = snacksList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.snacksList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Image image) {
        this.snacksList.add(image);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_column, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Image image = snacksList.get(position);
        String imageUrl;

        imageUrl = image.getImageUrl();

        Log.d(TAG, "onBindViewHolder: Glide Started");
        Glide.with(context)
                .load(imageUrl)
                .into(viewHolder.image);
        Log.d(TAG, "onBindViewHolder: Glide Ended");
    }

    @Override
    public int getCount() {
            return snacksList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        public ImageView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_fragment);
        }
    }

}