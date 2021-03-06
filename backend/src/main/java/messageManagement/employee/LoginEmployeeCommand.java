package messageManagement.employee;

import entities.Employee;
import dao.EmployeeDAO;
import messageManagement.Command;
import messageManagement.Message;
import messageManagement.Reply;
import messageManagement.ReplyType;

/**
 * Command to log in an employee by checking that inserted credentials match with a record in database
 */
public class LoginEmployeeCommand implements Command {
  @Override
  public synchronized Reply execute(Message message) {
    Reply replyMessage = null;
    Employee employee = null;

    try {
      Employee clientEmployee = (Employee) message.getUser();
      employee = EmployeeDAO.logIn(clientEmployee);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (employee != null) {
        replyMessage = new Reply(ReplyType.OK, employee);
      } else {
        replyMessage = new Reply(ReplyType.NOT_FOUND);
      }
    }

    return replyMessage;
  }
}
