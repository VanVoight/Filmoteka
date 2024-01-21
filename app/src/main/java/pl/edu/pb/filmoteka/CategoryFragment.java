package pl.edu.pb.filmoteka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    public CategoryFragment() {

    }
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        categoryList = generateCategories();
        categoryAdapter = new CategoryAdapter(categoryList, new OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {

                if (getActivity() instanceof OnCategoryClickListener) {
                    ((OnCategoryClickListener) getActivity()).onCategoryClick(category);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategories.setLayoutManager(layoutManager);
        recyclerViewCategories.setAdapter(categoryAdapter);

        return view;
    }

    private List<Category> generateCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.animated_ctg),16));
        categories.add(new Category(getString(R.string.drama_ctg),18));
        categories.add(new Category(getString(R.string.thriller_ctg),53));
        categories.add(new Category(getString(R.string.history_ctg),36));
        categories.add(new Category(getString(R.string.horror_ctg),27));
        categories.add(new Category(getString(R.string.romance_ctg),10749));
        categories.add(new Category(getString(R.string.comedy_ctg),35));
        categories.add(new Category(getString(R.string.musical_ctg),10402));
        categories.add(new Category(getString(R.string.sci_fi_ctg),878));
        categories.add(new Category(getString(R.string.fantasy_ctg),14));
        categories.add(new Category(getString(R.string.action_ctg),28));
        categories.add(new Category(getString(R.string.adventure_ctg),12));
        categories.add(new Category(getString(R.string.western_ctg),37));
        categories.add(new Category(getString(R.string.criminal_ctg),80));
        categories.add(new Category(getString(R.string.war_ctg),10752));
        categories.add(new Category(getString(R.string.mystery_ctg),9648));
        categories.add(new Category(getString(R.string.tv_movie_ctg),10770));
        categories.add(new Category(getString(R.string.family_ctg),10751));
        categories.add(new Category(getString(R.string.doc_ctg),99));

        return categories;
    }
}
