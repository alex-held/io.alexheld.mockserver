package io.alexheld.mockserver.logging



interface DataContainer<TData: DataContainerData> {
    var data: TData?
}

