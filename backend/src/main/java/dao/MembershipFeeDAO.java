package dao;

import common.DBUtil;
import entities.MembershipFee;
import entities.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class handles all the queries and interaction to MembershipFee entity of database
 */
public class MembershipFeeDAO {
  /**
   * Adds new Membership Fee record with PaymentType
   *
   * @param membershipFee Entity to be added to database
   */
  public static synchronized void insertMembershipFee(MembershipFee membershipFee) throws SQLException, ClassNotFoundException {
    String updateStmt = "INSERT INTO membership_fee (Member, Date, Fee, PaymentType) "
            + "VALUES ('" + membershipFee.getMember().getID() + "', '" + membershipFee.getDate()
            + "', '" + membershipFee.getFee() + "', '" + membershipFee.getPaymentType() + "')";

    try {
      DBUtil.dbExecuteUpdate(updateStmt);
    } catch (SQLException e) {
      System.out.print("Error occurred while INSERT Operation: " + e);
      throw e;
    }
  }

  /**
   * Get all Membership Fees in DB
   * @return ArrayList of MembershipFee
   */
  public static synchronized ArrayList<MembershipFee> getAllFees()
          throws SQLException, ClassNotFoundException {
    String selectStmt = "SELECT * FROM membership_fee";

    try {
      // Get ResultSet from dbExecuteQuery method
      ResultSet rsFees = DBUtil.dbExecuteQuery(selectStmt);

      return getMembershipFeesFromResultSet(rsFees);
    } catch (SQLException e) {
      System.out.println("While searching a membership fee, an error occurred: " + e);
      // Return exception
      throw e;
    }
  }

  /**
   * Get the only Membership Fee that is not expired
   * @param memberID The owner of MembershipFee
   * @return Found MembershipFee not expired, null otherwise
   */
  public static synchronized MembershipFee searchNotExpiredMembershipFeeByMember(Integer memberID)
          throws SQLException, ClassNotFoundException {
    String selectStmt = "SELECT * FROM membership_fee WHERE timestampdiff(second, Date, NOW()) < 30 AND Member='"
            + memberID + "'";

    try {
      // Get ResultSet from dbExecuteQuery method
      ResultSet rsFees = DBUtil.dbExecuteQuery(selectStmt);

      return getMembershipFeeFromResultSet(rsFees);
    } catch (SQLException e) {
      System.out.println("While searching a membership fee, an error occurred: " + e);
      // Return exception
      throw e;
    }
  }

  private static ArrayList<MembershipFee> getMembershipFeesFromResultSet(ResultSet rs)
      throws SQLException, ClassNotFoundException {
    // Declare an observable List which comprises Membership Fee objects
    ArrayList<MembershipFee> feesList = new ArrayList<>();

    while (rs.next()) {
      Member member = MemberDAO.searchMemberByID(rs.getInt("Member"));

      MembershipFee membershipFee = new MembershipFee(member, rs.getTimestamp("Date"), rs.getDouble("Fee"),
              rs.getString("PaymentType"));
      membershipFee.setID(rs.getInt("ID"));

      feesList.add(membershipFee);
    }

    return feesList;
  }

  private static MembershipFee getMembershipFeeFromResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {
    MembershipFee membershipFee = null;

    if (rs.next()){
      Member member = MemberDAO.searchMemberByID(rs.getInt("Member"));

      membershipFee = new MembershipFee(member, rs.getTimestamp("Date"), rs.getDouble("Fee"),
              rs.getString("PaymentType"));
      membershipFee.setID(rs.getInt("ID"));
    }

    return membershipFee;
  }
}
