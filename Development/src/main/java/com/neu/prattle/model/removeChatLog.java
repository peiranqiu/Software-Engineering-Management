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

public class removeChatLog {
  private Logger logger = Logger.getLogger(Message.class.getName());

  /***
   * The directory of massage folder
   */
  private String messagePath;
  /***
   * Get the parent directory of the message folder
   */

  /**
   * The group service.
   */
  private GroupService groupService = GroupServiceImpl.getInstance();

  public String getMessagePath() {
    String path = Message.class.getResource("").getPath();
    String mainPath = path.substring(0, path.indexOf("Development") + 11);
    return mainPath + "/src";
  }

  /***
   * Set the parent directory of the message folder
   */
  public void setMessagePath() {
    this.messagePath = getMessagePath();
  }

  public void deleteBeforeOneMonth(String groupName) {
    Optional<Group> group = groupService.findGroupByName(groupName);
    int groupId = group.get().getGroupId();
    try {
      // path of folder to check
      String extension = "txt";
      String startWith = Integer.toString(groupId);
      Date today = new Date();
      Calendar cal = new GregorianCalendar();
      cal.setTime(today);
      cal.add(Calendar.DAY_OF_MONTH, -30);
      Date lastThirtyDate = cal.getTime();
      LocalDate beforeDate = lastThirtyDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();

      String path = messagePath + "/Group";
      List<Path> paths = Files.list(Paths.get(path)).collect(Collectors.toList());

      for (Path entry : paths) {
        BasicFileAttributes attributes = Files.readAttributes(entry, BasicFileAttributes.class);
//        System.out.println("\tCreation Time/UTC Time: " + attributes.creationTime());
        Instant instant = Instant.parse(attributes.lastModifiedTime().toString());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
//        System.out.println("\tLocal Date: " + ldt.toLocalDate());
//        System.out.println("\tBefore Date: " + beforeDate);
        if (entry.getFileName().toString().startsWith(startWith) && entry.getFileName().toString().endsWith(extension) && ldt.toLocalDate().isBefore(beforeDate)) {
          System.out.println("\tMarked for deletion: Yes");
        }
        else {
          System.out.println("\tMarked for deletion: No");
        }

        System.out.println("\n");
      }
    } catch (IOException ex) {
      logger.info("Not able to file before last one month");
    }
  }
}
