package com.amazon.ata.metrics.classroom.activity;

import com.amazon.ata.metrics.classroom.dao.ReservationDao;
import com.amazon.ata.metrics.classroom.dao.models.Reservation;
import com.amazon.ata.metrics.classroom.metrics.MetricsConstants;
import com.amazon.ata.metrics.classroom.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;

/**
 * Handles requests to book a reservation.
 */
public class BookReservationActivity {

    private ReservationDao reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Constructs a BookReservationActivity
     * @param reservationDao Dao used to create reservations.
     */
    @Inject
    public BookReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates a reservation with the provided details.
     * @param reservation Reservation to create.
     * @return
     */
    public Reservation handleRequest(Reservation reservation) {

        Reservation response = reservationDao.bookReservation(reservation);
        // Update the Booked ReservationCount metric count
          // class-of-enum.enum-name
        metricsPublisher.addMetric (MetricsConstants. BOOKED_RESERVATION_COUNT, 1, StandardUnit.Count);
        // Update the ReservationRevenue metric with the total cost of the Reservation.
        // The Reservation is stored in response upon return from the ReservationDao
        metricsPublisher.addMetric (MetricsConstants. RESERVATION_REVENUE
                , response.getTotalCost().doubleValue(), // convert BigDecimal to double for method call
                StandardUnit.None);
        return response;
    }
}
