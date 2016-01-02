package jahirfiquitiva.apps.iconshowcase.muzei;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import jahirfiquitiva.apps.iconshowcase.R;
import jahirfiquitiva.apps.iconshowcase.models.WallpaperItem;
import jahirfiquitiva.apps.iconshowcase.utilities.JSONParser;
import jahirfiquitiva.apps.iconshowcase.utilities.Preferences;
import jahirfiquitiva.apps.iconshowcase.utilities.Util;

public class ArtSource extends RemoteMuzeiArtSource {

    private ArrayList<WallpaperItem> wallsInfoList = new ArrayList<>();

    private Preferences mPrefs;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> authors = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();

    private static final String ARTSOURCE_NAME = "IconShowcase";
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private static final int COMMAND_ID_SHARE = 1337;

    public ArtSource() {
        super(ARTSOURCE_NAME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getExtras().getString("service");
        if (command != null) {
            try {
                onTryUpdate(UPDATE_REASON_USER_NEXT);
            } catch (RetryException e) {
                Log.v("MuzeiArtSource", Log.getStackTraceString(e));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = new Preferences(ArtSource.this);
        ArrayList<UserCommand> commands = new ArrayList<>();
        commands.add(new UserCommand(BUILTIN_COMMAND_ID_NEXT_ARTWORK));
        commands.add(new UserCommand(COMMAND_ID_SHARE, getString(R.string.share)));
        setUserCommands(commands);

    }

    @Override
    public void onCustomCommand(int id) {
        super.onCustomCommand(id);
        if (id == COMMAND_ID_SHARE) {
            Artwork currentArtwork = getCurrentArtwork();
            Intent shareWall = new Intent(Intent.ACTION_SEND);
            shareWall.setType("text/plain");
            String wallName = currentArtwork.getTitle();
            String authorName = currentArtwork.getByline();
            String storeUrl = MARKET_URL + getPackageName();
            String iconPackName = getString(R.string.app_name);
            shareWall.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_text, wallName, authorName, iconPackName, storeUrl));
            shareWall = Intent.createChooser(shareWall, getString(R.string.share_title));
            shareWall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(shareWall);
        }
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        if (mPrefs.areFeaturesEnabled()) {
            try {
                new DownloadJSONAndSetWall().execute();
            } catch (Exception e) {
                throw new RetryException();
            }
        }

    }

    private void setImageForMuzei(String name, String author, String url) {
        publishArtwork(new Artwork.Builder()
                .title(name)
                .byline(author)
                .imageUri(Uri.parse(url))
                .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                .build());
        scheduleUpdate(System.currentTimeMillis() + mPrefs.getRotateTime());
        Util.showLog("Muzei Update scheduled to: " + String.valueOf(System.currentTimeMillis() + mPrefs.getRotateTime()));
    }

    public class DownloadJSONAndSetWall extends AsyncTask<Void, String, Boolean> {

        public JSONObject mainObject, wallItem;
        public JSONArray wallInfo;

        public DownloadJSONAndSetWall() {
        }

        @Override
        protected void onPreExecute() {
            names.clear();
            authors.clear();
            urls.clear();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean worked;
            try {
                mainObject = JSONParser
                        .getJSONfromURL(getResources().getString(R.string.json_file_url));
                if (mainObject != null) {
                    try {
                        wallInfo = mainObject.getJSONArray("wallpapers");
                        for (int i = 0; i < wallInfo.length(); i++) {
                            wallItem = wallInfo.getJSONObject(i);
                            names.add(wallItem.getString("name"));
                            authors.add(wallItem.getString("author"));
                            urls.add(wallItem.getString("url"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                worked = true;
            } catch (Exception e) {
                worked = false;
                Log.e("Wall", "Error " + e.getMessage());
            }
            return worked;
        }

        @Override
        protected void onPostExecute(Boolean worked) {
            if (worked) {
                int i = new Random().nextInt(names.size());
                setImageForMuzei(names.get(i), authors.get(i), urls.get(i));
                Util.showLog("Setting picture: " + names.get(i));
            }

        }

    }

}