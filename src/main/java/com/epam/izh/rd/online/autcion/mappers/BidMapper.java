package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Bid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BidMapper implements RowMapper<Bid> {

    @Override
    public Bid mapRow(ResultSet resultSet, int i) throws SQLException {
        Bid bid = new Bid();
        bid.setBidId(resultSet.getLong(1));
        bid.setBidDate(resultSet.getDate(2).toLocalDate());
        bid.setBidValue(resultSet.getDouble(3));
        bid.setItemId(resultSet.getLong(4));
        bid.setUserId(resultSet.getLong(5));
        return bid;
    }
}
