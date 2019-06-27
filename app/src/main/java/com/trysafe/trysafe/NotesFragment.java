package com.trysafe.trysafe;


import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trysafe.trysafe.Adapters.GridLayoutAdapter;
import com.trysafe.trysafe.Adapters.NotesAdapter;
import com.trysafe.trysafe.Models.NotesModel;
import com.trysafe.trysafe.NoteComponent.NodeAdapter;
import com.trysafe.trysafe.NoteComponent.Note;
import com.trysafe.trysafe.NoteComponent.NoteRepository;
import com.trysafe.trysafe.NoteComponent.NoteViewModel;
import com.trysafe.trysafe.SqlLiteHelper.DBManager;
import com.trysafe.trysafe.Utils.SnackbarHelper;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    public NotesFragment() {
        // Required empty public constructor
    }



    private ImageView add_notes;

    private static final int SCROLL_DIRECTION_UP = -1;


    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;

    CoordinatorLayout coordinatorLayout;

    LinearLayout emptyLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);

        final Toolbar toolbar = view.findViewById(R.id.appBar);

        Fade fade = new Fade();
        View decore = getActivity().getWindow().getDecorView();
        fade.excludeTarget(decore.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getActivity().getWindow().setEnterTransition(fade);
        getActivity().getWindow().setExitTransition(fade);

        coordinatorLayout = view.findViewById(R.id.cdlayout);
        emptyLayout = view.findViewById(R.id.empty_layout);


        add_notes = view.findViewById(R.id.add_notes);

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNotesActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
                (getActivity()).overridePendingTransition(R.anim.side_from_right,R.anim.sideout_from_left);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(24,0,24,0);
        final NodeAdapter adapter = new NodeAdapter();
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(SCROLL_DIRECTION_UP)) {
                    toolbar.setElevation(0);
                } else {
                    toolbar.setElevation(16);
                }
            }
        });



        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        noteViewModel.getNoteCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer > 0){
                    emptyLayout.setVisibility(View.INVISIBLE);
                }else {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

                final Note note = adapter.getNoteAt(viewHolder.getAdapterPosition());
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
           //     Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Note archived",8000)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                noteViewModel.insert(note);
                                adapter.notifyItemInserted(i);
                            }
                        }).setActionTextColor(Color.parseColor("#f4d03f"));

                SnackbarHelper.configSnackbar(getContext(), snackbar);

                snackbar.show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemCLickListener(new NodeAdapter.onItemCLickListener() {
            @Override
            public void onItemClick(View view, Note note) {

                TextView mTitle = view.findViewById(R.id.title_view);
                TextView mDescription = view.findViewById(R.id.des_view);

                Intent intent = new Intent(getContext(), AddNotesActivity.class);
                intent.putExtra(AddNotesActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddNotesActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddNotesActivity.EXTRA_DESCRIPTION, note.getDescription());



                Pair<View, String> pair = Pair.create((View)mTitle, ViewCompat.getTransitionName(mTitle));
                Pair<View, String> pair1 = Pair.create((View)mTitle, ViewCompat.getTransitionName(mDescription));

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        pair,
                        pair1
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, EDIT_NOTE_REQUEST,optionsCompat.toBundle());
                }else {
                    startActivityForResult(intent, EDIT_NOTE_REQUEST);
                }
            }

        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNotesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNotesActivity.EXTRA_DESCRIPTION);

            Note note = new Note(title, description,DBquery.userUid);
            noteViewModel.insert(note);


          //  Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddNotesActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddNotesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNotesActivity.EXTRA_DESCRIPTION);

            Note note = new Note(title, description,DBquery.userUid);
            note.setId(id);
            noteViewModel.update(note);

        } else if (requestCode == ADD_NOTE_REQUEST ) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Empty note cannot save",Snackbar.LENGTH_SHORT);
            SnackbarHelper.configSnackbar(getContext(), snackbar);
            snackbar.show();
        }

    }


}
