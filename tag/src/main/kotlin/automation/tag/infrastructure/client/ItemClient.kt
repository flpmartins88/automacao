package automation.tag.infrastructure.client

import automation.tag.infrastructure.Item
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 *
 *
 * @author Felipe Martins
 */
@FeignClient(url = "\${services.item}", name = "item-service")
interface ItemClient {

    /**
     * Gets data from item
     *
     * @param id Item's ID
     *
     * @return [Item] data
     */
    @RequestMapping(method = [RequestMethod.GET], value = ["/{id}"])
    fun getItem(@PathVariable id: Long): Item?
}
