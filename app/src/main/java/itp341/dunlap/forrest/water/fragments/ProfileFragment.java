package itp341.dunlap.forrest.water.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import itp341.dunlap.forrest.water.BuildConfig;
import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.activities.MainActivity;
import itp341.dunlap.forrest.water.activities.SplashActivity;
import itp341.dunlap.forrest.water.singletons.UserManager;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton menuFAB;
    FloatingActionMenu editFAB;

    TextView tv_username;

    StorageReference profileImageReference;

    FloatingActionButton signOutButton;
    FloatingActionButton changePictureButton;

    //TODO: Make profile picture work. :D
    CircleImageView profileImageView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        menuFAB = (FloatingActionButton) v.findViewById(R.id.menu_fab);
        menuFAB.setOnClickListener(this);

        editFAB = (FloatingActionMenu) v.findViewById(R.id.edit_fab);

        changePictureButton = new FloatingActionButton(getContext());
        changePictureButton.setOnClickListener(this);
        changePictureButton.setLabelText(getString(R.string.change_profile));
        changePictureButton.setImageResource(R.drawable.ic_action_profile_picture);
        changePictureButton.setColorNormalResId(R.color.colorPrimaryDark);
        changePictureButton.setColorPressedResId(R.color.colorPrimary);
        changePictureButton.setColorRippleResId(R.color.colorSecondary);
        changePictureButton.setId(R.id.changePictureButton);

        signOutButton = new FloatingActionButton(getContext());
        signOutButton.setOnClickListener(this);
        signOutButton.setLabelText(getString(R.string.sign_out));
        signOutButton.setImageResource(R.drawable.ic_action_arrow_back);
        signOutButton.setColorNormalResId(R.color.colorPrimaryDark);
        signOutButton.setColorPressedResId(R.color.colorPrimary);
        signOutButton.setColorRippleResId(R.color.colorSecondary);
        signOutButton.setId(R.id.signOutButton);

        editFAB.addMenuButton(signOutButton);
        editFAB.addMenuButton(changePictureButton);
        editFAB.setOnClickListener(this);

        tv_username = (TextView) v.findViewById(R.id.profile_name_tv);

        profileImageView = (CircleImageView) v.findViewById(R.id.circular_profile_image);
        profileImageView.setOnClickListener(this);

        profileImageReference = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child(UserManager.getInstance().getUserId())
                .child("profile");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>(){
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(getContext()).load(bytes).asBitmap().into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if(UserManager.getInstance().getCurrentUser() != null){
            tv_username.setText(UserManager.getInstance().getCurrentUser().getUsername());
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == menuFAB.getId())
            ((MainActivity)getActivity()).onQuestionInteraction(getResources().getInteger(R.integer.menu_drawer_action));
        if (v.getId() == signOutButton.getId()) {
            /*
            TODO: Create context menu that shows a context menu for 'what would you like to edit' & have sign-out.
             */
            if(BuildConfig.DEBUG) {
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Toast.makeText(getContext(), "Successfully signed out.", Toast.LENGTH_SHORT).show();
                    UserManager.getInstance().logOut();
                    getActivity().startActivity(new Intent(getContext(), SplashActivity.class));
                }

            } else {
                //TODO: Show edit menu
            }
        }

        if(v.getId() == changePictureButton.getId() || v.getId() == profileImageView.getId()){
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(getActivity().getPackageManager()) != null){
                startActivityForResult(takePicture, getResources().getInteger(R.integer.PICTURE_REQUEST));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == getResources().getInteger(R.integer.PICTURE_REQUEST) && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteData = baos.toByteArray();

            Glide.with(getContext()).load(byteData).asBitmap().dontAnimate().into(profileImageView);

            UploadTask uploadTask = profileImageReference.putBytes(byteData);
        }

    }
}
