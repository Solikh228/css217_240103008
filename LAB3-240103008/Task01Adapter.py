from abc import ABC, abstractmethod
from decimal import Decimal
from typing import Optional

class LegacyBillingSystem:
    def charge_customer_in_cents(self, customer_id: int, amount_in_cents: int) -> None:
        print(f"Billed customer {customer_id}: {amount_in_cents} cents")

class IPaymentGateway(ABC):
    @abstractmethod
    def process_payment(self, customer_id: int, amount_in_dollars: Decimal) -> None:
        pass


class PaymentGatewayAdapter(IPaymentGateway):
    def __init__(self, legacy_billing_system: LegacyBillingSystem):
        self._legacy_billing_system = legacy_billing_system

    def process_payment(self, customer_id: int, amount_in_dollars: Optional[Decimal]) -> None:
        if amount_in_dollars is None or amount_in_dollars < Decimal("0"):
            raise ValueError("Amount in dollars must not be None or negative")

        amount_in_cents = int(round(amount_in_dollars * Decimal("100")))
        self._legacy_billing_system.charge_customer_in_cents(customer_id, amount_in_cents)


if __name__ == "__main__":
    legacy_system = LegacyBillingSystem()
    adapter = PaymentGatewayAdapter(legacy_system)
    
    adapter.process_payment(101, Decimal("10.50"))
        