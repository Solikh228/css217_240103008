from core.target_interface import ShippingRateProvider
from core.models import ShippingQuote
from core.exceptions import ShippingServiceException
from vendors.fast_freight import FastFreightCloud, CarrierPayload, CarrierHttpError

class FastFreightAdapter(ShippingRateProvider):
    """Object Adapter wrapping FastFreightCloud via composition."""

    EUR_TO_USD_EXCHANGE_RATE = 1.08

    def __init__(self, service: FastFreightCloud):
        self._service = service

    def get_quote(self, weight_kg: float, destination_zip: str) -> ShippingQuote:
        grams = int(weight_kg * 1000)

        payload = CarrierPayload(
            weight_grams=grams,
            postal_code_str=str(destination_zip).strip()
        )

        try:
            cost_eur = self._service.fetch_quote(payload)
        except CarrierHttpError as e:
            raise ShippingServiceException(f"FastFreight error: {e.message}") from e

        cost_usd = round(cost_eur * self.EUR_TO_USD_EXCHANGE_RATE, 2)

        return ShippingQuote(
            cost_usd=cost_usd,
            delivery_days=2,
            carrier_name="FastFreightCloud"
        )