class OldPostalSocketTimeout(Exception):
    """Simulated hardware/network socket error on legacy mainframe."""
    pass

class OldPostalService:
    """Third-party SDK with legacy procedural calling conventions."""
    
    def compute_fare(self, weight_oz: float, zip_code: int) -> dict:
        if weight_oz <= 0:
            raise ValueError("Weight must be strictly positive")
        if zip_code == 0 or zip_code > 99999:
            raise OldPostalSocketTimeout(f"Gateway timeout contacting ZIP routing mainframe for {zip_code}")
        
        fee_cents = int(500 + (weight_oz * 25))
        
        transit_hours = 48 + int(weight_oz // 50) * 12
        
        return {
            "status": 200,
            "data": {
                "fee_cents": fee_cents,
                "transit_hours": transit_hours
            }
        }