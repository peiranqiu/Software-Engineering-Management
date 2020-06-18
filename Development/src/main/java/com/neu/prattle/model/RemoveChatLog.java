package com.neu.prattle.model;

import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class RemoveChatLog {
  private Logger logger = Logger.getLogger(Message.class.getName());
  private GroupService groupService = GroupServiceImpl.getInstance();
  public void setGroupService(GroupService newgroupService) {
    groupService = newgroupService;
  }

  public String getMessagePath() {
    String path = Message.class.getResource("").getPath();
    String mainPath = path.substring(0, path.indexOf("Development") + 11);
    return mainPath + "/src";
  }

  public void deleteBeforeNumberOfDays(String groupName, int daysBefore) {
    Optional<Group> group = groupService.findGroupByName(groupName);
    int groupId = group.get().getGroupId();
    try {
      String startWith = Integer.toString(groupId);
      Date today = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(today);
      cal.add(Calendar.DAY_OF_MONTH, 0 - daysBefore);
      Date lastThirtyDate = cal.getTime();
      LocalDate beforeDate = lastThirtyDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();

      String path = getMessagePath() + "/Group";
      List<Path> paths = Files.list(Paths.get(path)).collect(Collectors.toList());

      for (Path entry : paths) {
        BasicFileAttributes attributes = Files.readAttributes(entry, BasicFileAttributes.class);
        Instant instant = Instant.parse(attributes.lastModifiedTime().toString());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localdate = localDateTime.toLocalDate();
        if (entry.getFileName().toString().startsWith(startWith) && localdate.isBefore(beforeDate)) {
          deleteGroupLog(groupId, entry, localdate);
        }
        else {
          System.out.println("\tMarked for deletion: No");
        }
      }
    } catch (IOException ex) {
      logger.info("Not able to delete group chat file before this time period");
    }
  }

  public void deleteGroupLog(int groupId, Path filePath, LocalDate localdate) {
    String output = "delete group chat history fails";
    String s1 = getMessagePath() + "/Group/" + groupId + "_" + localdate + ".txt";
    Path path1 = Paths.get(s1);
    try {
      Files.delete(path1);
    } catch (IOException e) {
      logger.info("Message Sender File could not be deleted.");
    }
  }
}
