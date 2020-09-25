package com.football.matches.livescores.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.football.matches.livescores.R;
import com.football.matches.livescores.pojo.Article;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.article_title)
    TextView articleTitle;
    @BindView(R.id.article_source)
    TextView articleSource;
    @BindView(R.id.article_date)
    TextView articleDate;
    @BindView(R.id.article_image)
    ImageView articleImage;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.article_description)
    TextView articleDescription;
    @BindView(R.id.article_content)
    TextView articleContent;
    @BindView(R.id.article_author)
    TextView articleAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        String json = getIntent().getStringExtra("article");
        Article article = new Gson().fromJson(json, Article.class);
        setArticleData(article);
    }

    private void setContentSpannaple(Article article) {
        String content = article.getContent() + ", See All";
        SpannableString spannableString = new SpannableString(content);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, content.indexOf("See All"), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        articleContent.setText(spannableString);
        articleContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setArticleData(Article article) {

        if (article.getUrlToImage() != null) {
            Picasso.get().load(article.getUrlToImage()).fit().centerCrop().into(articleImage);
        } else
            articleImage.setImageResource(R.drawable.no_image);

        articleTitle.setText(article.getTitle());
        articleDescription.setText(article.getDescription());
        setContentSpannaple(article);
        articleSource.setText(article.getSource().getName());
        articleAuthor.setText("By : " + article.getAuthor());
        String arr[] = TimeFormat.dateFormat(article.getPublishedAt());
        String publishedDate = TimeFormat.formatToYesterdayOrToday(arr[0]) + ", " + arr[1];
        articleDate.setText(publishedDate);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}