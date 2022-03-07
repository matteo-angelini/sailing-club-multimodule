package entities;

public class NotifyMembershipFee {
    private Integer ID;
    private Member member;
    private Integer sent;

    /**
     * Constructor without ID
     * @param member Member to notify
     * @param sent Parameter set if the employee already sent the notification
     */
    public NotifyMembershipFee(Member member, boolean sent){
        this.member = member;
        if (!sent) this.sent = 0;
        else this.sent = 1;
    }

    /**
     * Constructor with ID
     * @param member Member to notify
     * @param sent Parameter set if the employee already sent the notification
     */
    public NotifyMembershipFee(Integer ID, Member member, boolean sent){
        this.ID = ID;
        this.member = member;
        if (!sent) this.sent = 0;
        else this.sent = 1;
    }

    /**
     * Getter for ID of NotifyMembershipFee
     * @return ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * Setter for ID of NotifyMembershipFee
     * @param ID ID to be put
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Getter for Member entity of NotifyMembershipFee
     * @return Member
     */
    public Member getMember() {
        return member;
    }

    /**
     * Setter for Member of NotifyMembershipFee
     * @param member Member to be put
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Getter for Sent of NotifyMembershipFee
     * @return Sent
     */
    public Integer getSent() {
        return sent;
    }

    /**
     * Setter for Sent of NotifyMembershipFee
     * @param sent Boolean value to be put
     */
    public void setSent(boolean sent) {
        if (!sent) this.sent = 0;
        else this.sent = 1;
    }
}
