package br.com.magalu.challenger.scheduler.domains.entities;

import java.util.Calendar;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "schedules")
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  @Column(name = "date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar sendDate;

  @NotNull @OneToOne private Recipient recipient;

  @NotNull
  @Column(nullable = false)
  private String message;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 8)
  private SendType sendType;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 9)
  private ScheduleStatus scheduleStatus;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Calendar createdAt;

  @Column(name = "modified_at")
  private Calendar modifiedAt;
}
