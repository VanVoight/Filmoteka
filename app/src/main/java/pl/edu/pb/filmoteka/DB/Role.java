package pl.edu.pb.filmoteka.DB;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
    @Entity(tableName = "roles")
    public class Role {
        @PrimaryKey
        private long roleId;
        private String roleName;
        public Role() {
        }
        public Role(long roleId, String roleName) {
            this.roleId = roleId;
            this.roleName = roleName;
        }
        public long getRoleId() {
            return roleId;
        }

        public void setRoleId(long roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }

