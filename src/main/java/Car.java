import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Car {
    private String carName;
    private String carType;
    private String companyName;

    @Builder
    public Car(String carName,
               String carType,
               String companyName) {
        this.carName = carName;
        this.carType = carType;
        this.companyName = companyName;
    }
}
