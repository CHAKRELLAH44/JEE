package ma.formations.msa.account_service.entities;



import lombok.*;



@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
}