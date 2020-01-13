package com.gmail.tikrai.books.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbInitListener implements ApplicationListener<ContextRefreshedEvent> {

  private final JdbcTemplate db;

  @Autowired
  public DbInitListener(JdbcTemplate db) {
    this.db = db;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    String sql = "CREATE TABLE IF NOT EXISTS books("
        + "   barcode VARCHAR (255) PRIMARY KEY,"
        + "   name VARCHAR (255) NOT NULL,"
        + "   author VARCHAR (255) NOT NULL,"
        + "   quantity INTEGER NOT NULL,"
        + "   price INTEGER NOT NULL,"
        + "   antique_release_year INTEGER,"
        + "   science_index INTEGER"
        + ");";
    db.update(sql);
  }
}
