package com.newscorp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ImagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Unbinder unbinder;
    public ImagesAdapter imagesAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static int GRID_SPAN_COUNT = 3;

    @BindView(R.id.imagesRecyclerView) RecyclerView imagesRecyclerView;
    @BindView(R.id.imagesSwipeRefresh) SwipeRefreshLayout swipeRefreshLayout;

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesAdapter = new ImagesAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        unbinder = ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        imagesRecyclerView.setNestedScrollingEnabled(true);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN_COUNT));
        updateSpans();
        imagesRecyclerView.setAdapter(imagesAdapter);
        imagesRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        imagesRecyclerView.getItemAnimator().setAddDuration(500);
        imagesRecyclerView.getItemAnimator().setRemoveDuration(500);
        imagesRecyclerView.getItemAnimator().setMoveDuration(500);
        imagesRecyclerView.getItemAnimator().setChangeDuration(500);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ApiService.getImages(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ImagesApiResponse imagesApiResponse) {
        imagesAdapter.update(imagesApiResponse.images);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        imagesAdapter.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiService.getImages(getContext());
            }
        }, 500);
    }

    private void completeAdapterRefresh() {
        imagesAdapter.clear();
        swipeRefreshLayout.setRefreshing(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Need to clear the entire adapter to clear all the ViewHolders
                //This is similar to onRefresh, but the entire Adapter is refreshed also
                imagesAdapter = new ImagesAdapter(ImagesFragment.this);
                imagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN_COUNT));
                imagesRecyclerView.setAdapter(imagesAdapter);
                ApiService.getImages(getContext());
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        ImagesViewHolder viewHolder = (ImagesViewHolder) imagesRecyclerView.findContainingViewHolder(imagesRecyclerView.findContainingItemView(view));
        FullScreenImageActivity.showImage(getActivity(), viewHolder.getBinding().image, viewHolder.getBinding().imageTitle, viewHolder.getBinding().getImageModel(), viewHolder.getWidth());

    }

    public static class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

        private WeakReference<ImagesFragment> context;
        private List<ImageModel> images;

        public ImagesAdapter(ImagesFragment context) {
            this.context = new WeakReference<>(context);
        }

        public void clear() {
            if (images != null) {
                int size = images.size();
                notifyItemRangeRemoved(0, size);
                images = null;
            }
        }

        public void update(List<ImageModel> images) {
            boolean empty = this.images == null;
            this.images = images;
            if (empty) {
                notifyItemRangeInserted(0, this.images.size());
            } else {
                notifyDataSetChanged();
            }
        }

        @Override
        public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImagesViewHolder(LayoutInflater.from(context.get().getContext()).inflate(R.layout.image_layout, parent, false), GRID_SPAN_COUNT);
        }

        @Override
        public void onBindViewHolder(ImagesViewHolder holder, int position) {
            holder.getBinding().setImagesFragment(context.get());
            holder.getBinding().setImageModel(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images == null ? 0 : images.size();
        }
    }

    private void updateSpans() {
        if (getActivity() instanceof MainActivity) {
            //Default for MainActivity
            ((MainActivity) getActivity()).gridSpans.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (GRID_SPAN_COUNT != position + 1) {
            GRID_SPAN_COUNT = position + 1;
            completeAdapterRefresh();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}