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
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        categoryList = generateCategories(); // Generate category list
        categoryAdapter = new CategoryAdapter(categoryList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategories.setLayoutManager(layoutManager);
        recyclerViewCategories.setAdapter(categoryAdapter);

        return view;
    }

    private List<Category> generateCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.animated_ctg)));
        categories.add(new Category(getString(R.string.drama_ctg)));
        categories.add(new Category(getString(R.string.thriller_ctg)));
        categories.add(new Category(getString(R.string.history_ctg)));
        categories.add(new Category(getString(R.string.horror_ctg)));
        categories.add(new Category(getString(R.string.romance_ctg)));
        categories.add(new Category(getString(R.string.comedy_ctg)));
        categories.add(new Category(getString(R.string.musical_ctg)));
        categories.add(new Category(getString(R.string.sci_fi_ctg)));
        categories.add(new Category(getString(R.string.fantasy_ctg)));
        categories.add(new Category(getString(R.string.action_ctg)));
        categories.add(new Category(getString(R.string.adventure_ctg)));
        categories.add(new Category(getString(R.string.western_ctg)));
        categories.add(new Category(getString(R.string.criminal_ctg)));
        categories.add(new Category(getString(R.string.other_ctg)));
        return categories;
    }
}
