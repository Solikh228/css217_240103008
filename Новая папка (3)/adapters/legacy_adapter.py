import math
from core.target_interface import ShippingRateProvider
from core.models import ShippingQuote
from core.exceptions import ShippingServiceException
from vendors.legacy_postal import OldPostalService, OldPostalSocketTimeout

class OldPostalAdapter(ShippingRateProvider):
    """Object Adapter wrapping OldPostalService via composition."""

    def __init__(self, service: OldPostalService):
        self._service = service

    def get_quote(self, weight_kg: float, destination_zip: str) -> ShippingQuote:
        try:
            zip_int = int(destination_zip)
        except ValueError as e:
            raise ShippingServiceException(f"Invalid ZIP code format: {destination_zip}") from e

        weight_oz = weight_kg * 35.274

        try:
            result = self._service.compute_fare(weight_oz, zip_int)
        except (OldPostalSocketTimeout, ValueError) as e:
            raise ShippingServiceException(f"Legacy postal error: {e}") from e

        fee_cents = result["data"]["fee_cents"]
        transit_hours = result["data"]["transit_hours"]

        cost_usd = round(fee_cents / 100.0, 2)
        delivery_days = max(1, math.ceil(transit_hours / 24.0))

        return ShippingQuote(
            cost_usd=cost_usd,
            delivery_days=delivery_days,
            carrier_name="OldPostal Service"
        )