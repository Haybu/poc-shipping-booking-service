import org.springframework.cloud.contract.spec.Contract

def contractDsl = Contract.make {
    label 'booking_created'
    input {
        triggeredBy('bookingCreated()')
    }
    outputMessage {
        sentTo('output')
        body('''
            {
              "bookingId": "123456789",
              "customerId": "12345",
              "occurredOn": "2010-10-10 10:10:10",
              "type": "BOOKING_CREATED",
              "cargoRequests": 
              [
                {
                  "requiredSize": byNull(),
                  "nature": byNull(),
                  "origin": 
                  {
                    "opZone": byNull()",
                    "facility": byNull()
                   },
                   "destination": 
                   {
                    "opZone": byNull(),
                    "facility": byNull()
                   },
                  "cutOffDate": byNull()
                }
              ]
            }
            ''')
        headers {
            header('event_type', 'BOOKING_CREATED')
            messagingContentType(applicationJson())
        }
    }
}