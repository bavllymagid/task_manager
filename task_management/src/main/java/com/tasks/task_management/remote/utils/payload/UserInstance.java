package com.tasks.task_management.remote.utils.payload;

import java.math.BigInteger;
import java.util.List;

public record UserInstance(BigInteger id, String username, String email, List<String> roles) {
}
