---
layout: page
title: Plants
permalink: "/plants/"
---

<style type="text/css">
.plants li {
	float: left;
	display: block;
	width: 50%;
	text-align: center;
}

.plants a {
	color: #ffffff !important;
	text-shadow: #000000 1px 1px !important;
}

.plants li div{
	padding: 10px;
	margin: 2px;
	height: 180px;
	line-height: 180px;
	background-clip: content-box;
    background-repeat: no-repeat;
    background-position: center;
    border-radius: 25px;
}

.plants li span {
	display: inline-block;
	vertical-align: middle;
	line-height: normal;
}

.plants li.odd {
	clear: left;
}
</style>

<ul class="plants">
{% for plant in site.data.plants %}
  <li class="{% cycle 'odd', 'even' %}">
  	<a href="{{ site.url }}{{ site.baseurl }}/plants/{{ plant.name | slugify }}/">
      <div style="background-image: url('../assets/{{ plant.id }}/thumb/{{ plant.images | last | split:'/' | last}}');background-size: cover;">
      	<span>{{ plant.name }} - {{ plant.strain }}</span>
      </div>
    </a>
  </li>
{% endfor %}
</ul>
