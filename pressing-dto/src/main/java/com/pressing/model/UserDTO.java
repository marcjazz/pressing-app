package com.pressing.model;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive;
  private ArrayList<Long> roleIds;
  private String telephone;
}
