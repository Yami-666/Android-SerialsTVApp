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

public class TvSerialAdapter extends RecyclerView.Adapter<TvSerialAdapter.TvSerialsViewHolder> {

    private List<TvSerials> mTvSerialsList;
    private Context context;
    private TvSerialsListener mTvSerialsListener;

    private LayoutInflater mLayoutInflater;

    public TvSerialAdapter(Context context, List<TvSerials> mTvSerialsList,
                           TvSerialsListener mTvSerialsListener) {
        this.mTvSerialsListener = mTvSerialsListener;
        this.context = context;
        this.mTvSerialsList = mTvSerialsList;
    }



    @NonNull
    @Override
    public TvSerialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(context);
        }
        ItemContainerTvSerialsBinding tvSerialsBinding = DataBindingUtil.inflate(
                mLayoutInflater, R.layout.item_container_tv_serials, parent, false
        );
        return new TvSerialsViewHolder(tvSerialsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvSerialsViewHolder holder, int position) {
        holder.bindTvSerials(mTvSerialsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTvSerialsList.size();
    }

    class TvSerialsViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvSerialsBinding mItemContainerTvSerialsBinding;

        public TvSerialsViewHolder(ItemContainerTvSerialsBinding mItemContainerTvSerialsBinding) {
            super(mItemContainerTvSerialsBinding.getRoot());
            this.mItemContainerTvSerialsBinding = mItemContainerTvSerialsBinding;
        }

        public void bindTvSerials(TvSerials tvSerials) {
            mItemContainerTvSerialsBinding.setTvSerials(tvSerials);
            mItemContainerTvSerialsBinding.executePendingBindings();
            mItemContainerTvSerialsBinding.getRoot().setOnClickListener(v -> mTvSerialsListener.onTvSerialClicked(tvSerials));
        }
    }
}
