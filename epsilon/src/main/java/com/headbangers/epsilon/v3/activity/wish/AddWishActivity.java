package com.headbangers.epsilon.v3.activity.wish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.activity.AbstractEpsilonActivity;
import com.headbangers.epsilon.v3.async.data.AutoCompleteDataAsyncLoader;
import com.headbangers.epsilon.v3.async.interfaces.Refreshable;
import com.headbangers.epsilon.v3.async.wish.AddWishAsyncLoader;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.AutoCompleteData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EActivity(R.layout.add_wish)
@OptionsMenu(R.menu.menu_ok)
public class AddWishActivity extends AbstractEpsilonActivity implements Refreshable<AutoCompleteData> {

    public static final int ADD_WISH_DONE = 300;
    public static final int REQUEST_IMAGE_CAPTURE = 301;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.category)
    AutoCompleteTextView category;

    @ViewById(R.id.account)
    Spinner account;

    @ViewById(R.id.price)
    TextView price;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.photo)
    ImageView photo;

    private List<Account> accounts;
    private String photoPath;

    @AfterViews
    void bindToolbar() {
        toolbar.setTitle(R.string.add_wish_title);
        toolbar.setSubtitle(R.string.add_whish_subtitle);
        setSupportActionBar(toolbar);

        init();
    }

    @EditorAction(R.id.name)
    void nameOk() {
        category.requestFocus();
    }

    @EditorAction(R.id.category)
    void categoryOk() {
        price.requestFocus();
    }

    @OptionsItem(R.id.action_ok)
    @EditorAction(R.id.price)
    void ok() {

        if (validateForm()) {
            String price = this.price.getText().toString();
            String category = this.category.getText().toString();
            String name = this.name.getText().toString();
            String account = this.accounts.get(this.account.getSelectedItemPosition()).getId();

            new AddWishAsyncLoader(accessService, this, progressBar).execute(name,
                    price, category, account, photoPath);
        }
    }

    @Click(R.id.camera)
    void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ADD_WISH", ex.getMessage());
                Toast.makeText(this, R.string.error_creating_photo, Toast.LENGTH_LONG).show();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.headbangers.epsilon.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @OnActivityResult(REQUEST_IMAGE_CAPTURE)
    void photoTaken() {
        if (photoPath != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(URI.create(photoPath))));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 0, new FileOutputStream(new File(URI.create(photoPath))));

                photo.setImageURI(Uri.parse(photoPath));
                photo.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                this.photoPath = null;
                photo.setVisibility(View.GONE);
            }

        } else {
            photo.setVisibility(View.GONE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "WISH_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir, imageFileName + ".jpg");

        photoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void init() {
        new AutoCompleteDataAsyncLoader(accessService, this, progressBar)
                .execute(AutoCompleteDataAsyncLoader.Load.CATEGORY_ACCOUNTS.toString());
    }

    @Override
    public void refresh(AutoCompleteData result) {
        if (result != null) {
            this.accounts = result.getAccounts();

            List<String> accountsName = new ArrayList<>();
            for (Account account : accounts) {
                accountsName.add(account.getName() + " - "
                        + df.format(account.getSold()) + "€");
            }

            ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    accountsName);
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.account.setAdapter(accountAdapter);

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_item, result.getCategories());
            this.category.setAdapter(categoryAdapter);
        }
    }

    private boolean validateForm() {
        String price = this.price.getText().toString();
        String category = this.category.getText().toString();
        String name = this.name.getText().toString();

        if (name == null || name.isEmpty()) {
            this.name.setError(errorFormName);
        }

        if (category == null || category.isEmpty()) {
            this.category.setError(errorFormCategory);
        }

        if (price == null || price.isEmpty()) {
            this.price.setError(errorFormAmount);
        }

        return price != null && !price.isEmpty()
                && category != null && !category.isEmpty()
                && name != null && !name.isEmpty();
    }
}
