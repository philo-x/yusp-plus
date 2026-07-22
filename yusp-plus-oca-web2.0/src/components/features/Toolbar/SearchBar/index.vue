<template>
  <div class="yu-frame-search" title="">
    <div class="yu-frame-search-type">
      <span>
        {{ searchType.name }}
        <i class="yu-icon-arr-down1"></i>
      </span>
      <ul class="yu-frame-dropdown-menu search-type-list">
        <li v-for="item in searchTypeList" :key="item.id" @click="searchTypeSwitch(item.id,$event)" :class="[item.checked?'yu-icon-checked2':'yu-icon-choice-un']" :title="item.name">
          {{ item.name }}
        </li>
      </ul>
    </div>
    <yu-input placeholder="关键字" v-model="searchValue" @input="searchDataFilter"></yu-input>
    <i class="yu-frame-search-button" :class="tool.icon" @click="searchBarClick"></i>
  </div>
</template>
