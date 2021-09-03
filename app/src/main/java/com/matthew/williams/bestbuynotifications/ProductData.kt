package com.matthew.williams.bestbuynotifications

class ProductData {
    companion object {
        val linkData: ArrayList<BestBuyProduct>
            get() = arrayListOf<BestBuyProduct>(
                BestBuyProduct(
                    "https://www.bestbuy.com/site/nvidia-geforce-rtx-3070-8gb-gddr6-pci-express-4-0-graphics-card-dark-platinum-and-black/6429442.p?skuId=6429442",
                    "3070 FE"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/gigabyte-nvidia-geforce-rtx-3070-eagle-8gb-gddr6-pci-express-4-0-graphics-card/6437912.p?skuId=6437912",
                    "3070 gigabyte"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/nvidia-geforce-rtx-3070-ti-8gb-gddr6x-pci-express-4-0-graphics-card-dark-platinum-and-black/6465789.p?skuId=6465789",
                    "3070ti FE"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/evga-geforce-rtx-3070-xc3-ultra-gaming-8gb-gddr6-pci-express-4-0-graphics-card-light-hash-rate/6477077.p?skuId=6477077",
                    "3070 evga"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/gigabyte-nvidia-geforce-rtx-3070ti-eagle-8gb-gddr6x-pci-express-4-0-graphics-card-black/6467782.p?skuId=6467782",
                    "3070ti gigabyte"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/evga-geforce-rtx-3070-xc3-ultra-gaming-8gb-gddr6-pci-express-4-0-graphics-card/6439299.p?skuId=6439299",
                    "3060 evga"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/evga-nvidia-geforce-rtx-3060-xc-gaming-12gb-gddr6-pci-express-4-0-graphics-card/6454329.p?skuId=6454329",
                    "3060 evga"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/nvidia-geforce-rtx-3060-ti-8gb-gddr6-pci-express-4-0-graphics-card-steel-and-black/6439402.p?skuId=6439402",
                    "3060ti FE"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/gigabyte-nvidia-geforce-rtx-3060-12gb-gddr6-pci-express-4-0-graphics-card/6468928.p?skuId=6468928",
                    "3060 gigabyte"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/asus-tuf-rtx3060ti-8gb-gddr6-pci-express-4-0-graphics-card/6452573.p?skuId=6452573",
                    "3060ti asus"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/gigabyte-nvidia-geforce-rtx-3060-12gb-gddr6-pci-express-4-0-graphics-card/6468931.p?skuId=6468931",
                    "3060 gigabyte"
                ),
                BestBuyProduct(
                    "https://www.bestbuy.com/site/gigabyte-nvidia-geforce-rtx-3060-eagle-oc-12gb-gddr6-pci-express-4-0-graphics-card/6454689.p?skuId=6454689",
                    "3060 gigabyte"
                ),
            )
    }
}

data class BestBuyProduct(val url: String, val description: String) {}