package messageManagement.member;

import dao.BoatDAO;
import dao.NotifyStorageFeeDAO;
import dao.StorageFeeDAO;
import entities.Boat;
import messageManagement.Command;
import messageManagement.Message;
import messageManagement.Reply;
import messageManagement.ReplyType;

/**
 * Command to delete a boat from database
 */
public class RemoveBoatCommand implements Command {
    @Override
    public synchronized Reply execute(Message message) {
        Reply replyMessage = null;

        try {
            Boat boatToDelete = (Boat) message.getNewObject();

            // delete notification that depends on selected boat
            NotifyStorageFeeDAO.deleteNotification(boatToDelete.getID());
            // delete boat
            BoatDAO.deleteBoat(boatToDelete.getID());
        } catch (Exception e) {
            replyMessage = new Reply(ReplyType.ERROR);
            return replyMessage;
        } finally {
            replyMessage = new Reply(ReplyType.OK);
        }

        return replyMessage;
    }
}
