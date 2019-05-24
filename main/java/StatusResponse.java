public enum StatusResponse {
    SUCCESS ("SUCCESS"), ERROR ("ERROR");
    final private String status;
    StatusResponse(String status) { this.status = status; }

    public String getStatus() {
        return this.status;
    }
}
