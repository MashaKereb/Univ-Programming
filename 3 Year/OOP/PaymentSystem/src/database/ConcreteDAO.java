package database;

import beans.*;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConcreteDAO implements DAO {


    Connection connection;
    private static PGConnectionPoolDataSource source;
    static {
            source = new PGConnectionPoolDataSource();
            source.setServerName("localhost");
            source.setPortNumber(5432);
            source.setDatabaseName("java-lab");
            source.setUser("postgres");
            source.setPassword("MarketData");
            source.setDefaultAutoCommit(true);


    }
    private static synchronized Connection getConnection(){
        try {
            System.out.println("get");
            return (Connection) source.getPooledConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConcreteDAO() {
            connection = getConnection();

    }

    @Override
    public User getUser(String login, String password) {
        try {
            PreparedStatement preparedStatement=connection.prepareStatement("select * from users where login=? and password=?");
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                User user=new User();
                user.setAdmin(resultSet.getBoolean("admin"));
                user.setCardCode(resultSet.getString("card"));
                user.setId(resultSet.getInt("id"));
                user.setLogin(login);
                user.setPassword(password);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Menu getMenu() {
        ResultSet resultSet=null;
        Statement statement=null;
        try {
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select * from \n" +
                    "  ((menudish INNER JOIN dish on menudish.disid=dish.id)\n" +
                    "    inner join menu on menudish.menuid=menu.id )\n" +
                    "  where menu.id=1;");
            Menu menu=new Menu();
            resultSet.next();
            menu.setId(resultSet.getInt(1));
            menu.setName(resultSet.getString(7));

            List list=new ArrayList();
            do{
                Dish dish=new Dish();
                dish.setId(resultSet.getInt(3));
                dish.setName(resultSet.getString(5));
                dish.setPrice(resultSet.getInt(4));
                list.add(dish);
            }
            while (resultSet.next());
            menu.setDishes(list);
            return menu;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    public Bills getBills() {
        Statement statement=null;
        ResultSet resultSet=null;
        try {

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet=statement.executeQuery("SELECT * FROM \n" +
                    "  (BillDish INNER JOIN dish ON BillDish.dishid=dish.id)\n" +
                    "    INNER JOIN bill ON BillDish.billid=bill.id\n" +
                    "  WHERE bill.confirm=false ");

            Map billsMap=new HashMap();
            while (resultSet.next()){
                int id=resultSet.getInt(1);
                if (billsMap.get(id)!=null)
                    continue;
                System.out.println(id);
                Bill bill=new Bill();
                bill.setId(id);
                bill.setConfirm(false);
                bill.setPaid(false);
                bill.setPrice(resultSet.getInt(8));
                bill.setTable(resultSet.getInt(7));
                User user=new User();
                user.setId(resultSet.getInt(11));
                bill.setUser(user);
                billsMap.put(id,bill);
            }

            resultSet.first();


            while (resultSet.next()){
                Dish dish=new Dish();
                dish.setPrice(resultSet.getInt(4));
                dish.setName(resultSet.getString(5));
                dish.setId(resultSet.getInt(2));
                ((Bill) billsMap.get(resultSet.getInt(1))).addDish(dish);
            }


            return new Bills(new ArrayList(billsMap.values()));


        } catch (SQLException e) {
            e.printStackTrace();
        }        finally {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean addBill(Bill bill) {
        PreparedStatement statement= null;
        try {
            statement = connection.prepareStatement("INSERT INTO bill(\n" +
                    "            id, \"table\", price, confirm, padi, userid)\n" +
                    "   VALUES (default , ?, ?, ?, ?, ?) RETURNING id; ");
            statement.setInt(1,bill.getTable());
            statement.setInt(2,bill.getPrice());
            statement.setBoolean(3,bill.isConfirm());
            statement.setBoolean(4,bill.isPaid());
            statement.setInt(5,bill.getUser().getId());
            ResultSet resultSet=statement.executeQuery();
            resultSet.next();
            bill.setId(resultSet.getInt("id"));
            List<Dish> dishs=bill.getDishes();
            for (Dish dish:dishs){
                PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO billdish(\n" +
                        "            billid, dishid)\n" +
                        "    VALUES (?, ?);\n");
                preparedStatement.setInt(1,bill.getId());
                preparedStatement.setInt(2, dish.getId());
                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean setBillConfirm(Bill bill) {
        try {
            PreparedStatement statement=connection.prepareStatement("update bill set confirm=true where id=?");
            statement.setInt(1,bill.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setBillPaid(Bill bill) {
        try {
            PreparedStatement statement=connection.prepareStatement("update bill set padi=true where id=?");
            statement.setInt(1,bill.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Bills getUserBills(User user) {
        Statement statement=null;
        ResultSet resultSet=null;
        try {

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet=statement.executeQuery("SELECT * FROM \n" +
                    "  (BillDish INNER JOIN dish ON BillDish.dishid=dish.id)\n" +
                    "    INNER JOIN bill ON BillDish.billid=bill.id\n" +
                    "  WHERE bill.confirm=true and bill.padi=false and bill.userid="+user.getId());
            System.out.println(user.getId()+"USER ID");
            Map billsMap=new HashMap();
            while (resultSet.next()){
                int id=resultSet.getInt(1);
                if (billsMap.get(id)!=null)
                    continue;
                System.out.println(id);
                Bill bill=new Bill();
                bill.setId(id);
                bill.setConfirm(false);
                bill.setPaid(false);
                bill.setPrice(resultSet.getInt(8));
                bill.setTable(resultSet.getInt(7));
                bill.setUser(user);
                billsMap.put(id,bill);
            }

            resultSet.first();


            while (resultSet.next()){
                Dish dish=new Dish();
                dish.setPrice(resultSet.getInt(4));
                dish.setName(resultSet.getString(5));
                dish.setId(resultSet.getInt(2));
                ((Bill) billsMap.get(resultSet.getInt(1))).addDish(dish);
            }


            return new Bills(new ArrayList(billsMap.values()));


        } catch (SQLException e) {
            e.printStackTrace();
        }        finally {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
