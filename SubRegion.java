public class SubRegion extends Region {

    private Region mainRegion;

    public SubRegion(BlockingQueue<Region> regionQueue, Semaphore semaphore, Region mainRegion) {
        super(regionQueue, semaphore);
        this.mainRegion = mainRegion;
    }

    @Override
    public boolean canAddRegion(LatLng newRegion, LatLng currentLocation) {
        if (calculateDistance(mainRegion.regionQueue.peek(), newRegion) > 30) {
            return false;
        }
        return super.canAddRegion(newRegion, currentLocation);
    }
}
} 
