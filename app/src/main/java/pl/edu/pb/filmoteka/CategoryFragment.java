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
        categories.add(new Category("Animowane"));
        categories.add(new Category("Dramat"));
        categories.add(new Category("Thriller"));
        categories.add(new Category("Filmy historyczne"));
        categories.add(new Category("Horrory"));
        categories.add(new Category("Filmy romantyczne"));
        categories.add(new Category("Filmy komediowe"));
        categories.add(new Category("Filmy muzyczne"));
        categories.add(new Category("Filmy science fiction"));
        categories.add(new Category("Filmy fantasy"));
        categories.add(new Category("Filmy akcji"));
        categories.add(new Category("Filmy przygodowe"));
        categories.add(new Category("Westerny"));
        categories.add(new Category("Filmy kryminalne"));
        categories.add(new Category("Inne"));
        return categories;
    }
}
