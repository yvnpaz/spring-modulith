package victor.training.modulith.inventory.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;

@Getter
@ToString
@Entity
public class Stock extends AbstractAggregateRoot<Stock> {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Setter // + FK to PRODUCT.ID
  private Long productId;

//  @ManyToOne private Product product; // TODO uncomment

  @NotNull
  private Integer items = 0;

  public Stock add(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Negative: " + n);
    }
    if (items == 0) {
      registerEvent(new BackInStockEvent(productId));
    }
    items += n;
    return this;
  }

  public void remove(Integer delta) {
    if (delta <= 0) {
      throw new IllegalArgumentException("Negative: " + delta);
    }
    if (delta > items) {
      throw new IllegalArgumentException("Not enough stock to remove: " + delta);
    }
    items -= delta;
    if (items == 0) {
      registerEvent(new OutOfStockEvent(productId));
    }
  }
}
