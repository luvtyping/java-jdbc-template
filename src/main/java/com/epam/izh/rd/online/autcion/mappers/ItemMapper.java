package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        Item item = new Item();
        item.setItemId(resultSet.getLong(1));
        item.setBidIncrement(resultSet.getDouble(2));
        item.setBuyItNow(resultSet.getBoolean(3));
        item.setDescription(resultSet.getString(4));
        item.setStartDate(resultSet.getDate(5).toLocalDate());
        item.setStartPrice(resultSet.getDouble(6));
        item.setStopDate(resultSet.getDate(7).toLocalDate());
        item.setTitle(resultSet.getString(8));
        item.setUserId(resultSet.getLong(9));
        return item;
    }
}
