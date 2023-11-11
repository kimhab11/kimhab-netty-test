package time;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
public class UnixTime {
    private final long value;

    public UnixTime(){
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new Date((getValue() - 2208988800L) * 1000L).toString();
    }
}
