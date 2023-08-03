package Test04.jdbcTemplate;


import Test04.datasource.MyDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public  abstract class JdbcTemplate<T> {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<T> executeQuery(String sql, RowMapper<?> rowMapper, Object...params){
        List<T>list = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            setParams(pstmt, params);
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                T t = (T) rowMapper.mapper(rs, i);
                i++;
                list.add(t);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (rs!=null){
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            ((MyDataSource)dataSource).returnConnetion(con);
        }
        return list;
    }

    private   void setParams(PreparedStatement stmt, Object... params) throws SQLException {
        if (params==null||params.length<=0){
            return;
        }

        for (int i=0;i<params.length;i++){
            stmt.setObject(i+1,params[i]);
        }
    }
}
