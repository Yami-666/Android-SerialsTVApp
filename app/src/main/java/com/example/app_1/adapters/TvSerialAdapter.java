package com.example.app_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_1.R;
import com.example.app_1.databinding.ItemContainerTvSerialsBinding;
import com.example.app_1.listeners.TvSerialsListener;
import com.example.app_1.models.TvSerials;

import java.util.List;

public class TvSerialAdapter extends RecyclerView.Adapter<TvSerialAdapter.TvSerialViewHolder> {

    private List<TvSerials> tvSerialsList;
    private Context context;
    private TvSerialsListener tvSerialsListener;

    private LayoutInflater layoutInflater;

    public TvSerialAdapter(Context context, List<TvSerials> tvSerialsList, TvSerialsListener tvSerialsListener) {
        this.tvSerialsListener = tvSerialsListener;
        this.context = context;
        this.tvSerialsList = tvSerialsList;
    }



    @NonNull
    @Override
    public TvSerialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        ItemContainerTvSerialsBinding tvSerialsBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_serials, parent, false
        );
        return new TvSerialViewHolder(tvSerialsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvSerialViewHolder holder, int position) {
        holder.bindTvSerials(tvSerialsList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvSerialsList.size();
    }

    class TvSerialViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvSerialsBinding itemContainerTvSerialsBinding;

        public TvSerialViewHolder(ItemContainerTvSerialsBinding itemContainerTvSerialsBinding) {
            super(itemContainerTvSerialsBinding.getRoot());
            this.itemContainerTvSerialsBinding = itemContainerTvSerialsBinding;
        }

        public void bindTvSerials(TvSerials tvSerials) {
            itemContainerTvSerialsBinding.setTvSerials(tvSerials);
            itemContainerTvSerialsBinding.executePendingBindings();
            itemContainerTvSerialsBinding.getRoot().setOnClickListener(v -> tvSerialsListener.onTvSerialClicked(tvSerials));
        }
    }
}
