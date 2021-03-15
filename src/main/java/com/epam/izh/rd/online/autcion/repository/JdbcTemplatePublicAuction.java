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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Double averagePrice = jdbcTemplate.queryForObject("SELECT AVG(start_price) FROM items WHERE user_id=?",
                    Double.class, user.getUserId());
            if (averagePrice == null)
                continue;
            map.put(user, averagePrice);
        }
        return map;
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        List<Item> items = jdbcTemplate.query("SELECT * FROM items", itemMapper);
        Map<Item, Bid> map = new HashMap<>();
        for (Item item : items) {
            List<Bid> bids = jdbcTemplate.query(
                    "SELECT * FROM bids " +
                            "WHERE item_id=? AND bid_value=(SELECT MAX(bid_value) FROM bids WHERE item_id=?)",
                    bidMapper, item.getItemId(), item.getItemId());
            if (bids.isEmpty())
                continue;
            map.put(item, bids.get(0));
        }
        return map;
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
        int update = jdbcTemplate.update("INSERT INTO bids VALUES(?,?,?,?,?)",
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
        int update = jdbcTemplate.update("DELETE FROM bids WHERE user_id=?", id);
        return update != 0;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        int update = jdbcTemplate.update("UPDATE items SET start_price=(start_price*2) WHERE user_id=?", id);
        return update != 0;
    }
}
