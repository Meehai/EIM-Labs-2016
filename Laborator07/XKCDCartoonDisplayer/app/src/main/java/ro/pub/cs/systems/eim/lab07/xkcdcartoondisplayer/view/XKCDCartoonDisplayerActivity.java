package ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.R;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.entities.XKCDCartoonInformation;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.general.Constants;

public class XKCDCartoonDisplayerActivity extends AppCompatActivity {

    private TextView xkcdCartoonTitleTextView;
    private ImageView xkcdCartoonImageView;
    private TextView xkcdCartoonUrlTextView;
    private Button previousButton, nextButton;

    private class XKCDCartoonUrlButtonClickListener implements Button.OnClickListener {

        String xkcdComicUrl;

        public XKCDCartoonUrlButtonClickListener(String xkcdComicUrl) {
            this.xkcdComicUrl = xkcdComicUrl;
        }

        @Override
        public void onClick(View view) {
            new XKCDCartoonDisplayerAsyncTask().execute(xkcdComicUrl);
        }
    }

    private class XKCDCartoonDisplayerAsyncTask extends AsyncTask<String, Void, XKCDCartoonInformation> {

        @Override
        protected XKCDCartoonInformation doInBackground(String... urls) {
            XKCDCartoonInformation xkcdCartoonInformation = new XKCDCartoonInformation();

            // TODO: exercise 5a)
            // 1. obtain the content of the web page (whose Internet address is stored in urls[0])
            // - create an instance of a HttpClient object
            // - create an instance of a HttpGet object
            // - create an instance of a ResponseHandler object
            // - execute the request, thus obtaining the web page source code

            // 2. parse the web page source code
            // - cartoon title: get the tag whose id equals "ctitle"
            // - cartoon url
            //   * get the first tag whose id equals "comic"
            //   * get the embedded <img> tag
            //   * get the value of the attribute "src"
            //   * prepend the protocol: "http:"
            // - previous cartoon address
            //   * get the first tag whole rel attribute equals "prev"
            //   * get the href attribute of the tag
            //   * prepend the value with the base url: http://www.xkcd.com
            //   * attach the previous button a click listener with the address attached
            // - next cartoon address
            //   * get the first tag whole rel attribute equals "next"
            //   * get the href attribute of the tag
            //   * prepend the value with the base url: http://www.xkcd.com
            //   * attach the next button a click listener with the address attached

            return xkcdCartoonInformation;
        }

        @Override
        protected void onPostExecute(XKCDCartoonInformation xkcdCartoonInformation) {

            // TODO: exercise 5b)
            // map each member of xkcdCartoonInformation object to the corresponding widget
            // cartoonTitle -> xkcdCartoonTitleTextView
            // cartoonUrl -> xkcdCartoonUrlTextView
            // based on cartoonUrl fetch the bitmap using Volley (using an ImageRequest object added to the queue)
            // and put it into xkcdCartoonImageView
            // previousCartoonUrl, nextCartoonUrl -> set the XKCDCartoonUrlButtonClickListener for previousButton, nextButton

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xkcd_cartoon_displayer);

        xkcdCartoonTitleTextView = (TextView)findViewById(R.id.xkcd_cartoon_title_text_view);
        xkcdCartoonImageView = (ImageView)findViewById(R.id.xkcd_cartoon_image_view);
        xkcdCartoonUrlTextView = (TextView)findViewById(R.id.xkcd_cartoon_url_text_view);

        previousButton = (Button)findViewById(R.id.previous_button);
        nextButton = (Button)findViewById(R.id.next_button);

        new XKCDCartoonDisplayerAsyncTask().execute(Constants.XKCD_INTERNET_ADDRESS);
    }
}
