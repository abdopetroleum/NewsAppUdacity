package com.spe.networkingudacitylastproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News news = getItem(position);
        TextView title = listItemView.findViewById(R.id.news_title);
        TextView section = listItemView.findViewById(R.id.section);
        TextView date = listItemView.findViewById(R.id.date);
        TextView author = listItemView.findViewById(R.id.author);

        title.setText(news.getTitle());
        section.setText(news.getSection());
        date.setText(formatDate(news.getDate()));
        author.setText(news.getAuthor());
        return listItemView;
    }

    private static String formatDate(String encodedDate) {
        String encodingFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(encodingFormat, Locale.US);
        try {
            Date date = formatter.parse(encodedDate);
            String formattedDatePattern = "MMM d, yyy";
            SimpleDateFormat format = new SimpleDateFormat(formattedDatePattern, Locale.US);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}