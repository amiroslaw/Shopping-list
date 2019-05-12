package ovh.miroslaw.shoppinglist.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO representing a password change required data - current and new password.
 */
public class PasswordChangeDTO {
    private String currentPassword;
    @NotNull
    @Size(min = 6, max = 50)
    private String newPassword;

    public PasswordChangeDTO() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {

        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
