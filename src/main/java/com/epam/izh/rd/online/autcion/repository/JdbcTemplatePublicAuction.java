package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;
import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import com.epam.izh.rd.online.autcion.mappers.ItemMapper;
import com.epam.izh.rd.online.autcion.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Repository
public class JdbcTemplatePublicAuction implements PublicAuction {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Bid> getUserBids(long id) {
        return jdbcTemplate.query("SELECT * FROM bids WHERE user_id=?", bidMapper, id);
    }

    @Override
    public List<Item> getUserItems(long id) {
        return jdbcTemplate.query("SELECT * FROM items WHERE user_id=?", itemMapper, id);
    }

    @Override
    public Item getItemByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE title LIKE '" + name + "'", itemMapper);
    }

    @Override
    public Item getItemByDescription(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE description LIKE '" + name + "'", itemMapper);
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        List<User> userList = jdbcTemplate.query("SELECT * FROM users", userMapper);
        Map<User, Double> map = new HashMap<>();
        for (User user : userList) {
            Double price = jdbcTemplate.queryForObject("SELECT AVG(start_price) FROM items WHERE user_id=?",
                    Double.class, user.getUserId());
            if (price == null)
                continue;
            map.put(user, price);
        }
        return map;
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        return emptyMap();
    }

    @Override
    public boolean createUser(User user) {
        int update = jdbcTemplate.update("INSERT INTO users VALUES(?,?,?,?,?)",
                user.getUserId(),
                user.getBillingAddress(),
                user.getFullName(),
                user.getLogin(),
                user.getPassword());
        return update != 0;
    }

    @Override
    public boolean createItem(Item item) {
        int update = jdbcTemplate.update("INSERT INTO items VALUES(?,?,?,?,?,?,?,?,?,)",
                item.getItemId(),
                item.getBidIncrement(),
                item.getBuyItNow(),
                item.getDescription(),
                item.getStartDate(),
                item.getStartPrice(),
                item.getStopDate(),
                item.getTitle(),
                item.getUserId()
        );
        return update != 0;
    }

    @Override
    public boolean createBid(Bid bid) {
        int update = jdbcTemplate.update("INSERT INTO users VALUES(?,?,?,?,?)",
                bid.getBidId(),
                bid.getBidDate(),
                bid.getBidValue(),
                bid.getItemId(),
                bid.getUserId()
        );
        return update != 0;
    }

    @Override
    public boolean deleteUserBids(long id) {
        int update = jdbcTemplate.update("DELETE FROM bids WHERE user_id=?",
                id);
        return update != 0;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        int update = jdbcTemplate.update("UPDATE items SET start_price=(start_price*2) WHERE user_id=?", id);
        return update != 0;
    }

    @Override
    public List<Bid> getUserActualBids(long id) {
        return jdbcTemplate.query("SELECT COUNT(item_id) FROM items WHERE id IN (SELECT id FROM users WHERE user_id=?)", bidMapper, id);
    }
}
